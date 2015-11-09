package org.openlegacy.providers.storedproc;

import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.rpc.RpcConnection;
import org.openlegacy.rpc.RpcField;
import org.openlegacy.rpc.RpcFlatField;
import org.openlegacy.rpc.RpcInvokeAction;
import org.openlegacy.rpc.RpcResult;
import org.openlegacy.rpc.RpcSnapshot;
import org.openlegacy.rpc.RpcStructureField;
import org.openlegacy.rpc.RpcStructureListField;
import org.openlegacy.rpc.support.SimpleRpcFields;
import org.openlegacy.rpc.support.SimpleRpcFlatField;
import org.openlegacy.rpc.support.SimpleRpcResult;
import org.openlegacy.rpc.support.SimpleRpcStructureListField;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class StoredProcRpcConnection implements RpcConnection {

	private Integer sequence = 0;

	Connection dbConnection = null;

	private String dbUrl = null;
	private String dbDriverClassName = null;

	public StoredProcRpcConnection(String dbUrl, String dbDriverClassName) {
		this.dbUrl = dbUrl;
		this.dbDriverClassName = dbDriverClassName;
	}

	@Override
	public RpcSnapshot getSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RpcSnapshot fetchSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doAction(RpcInvokeAction sendAction) {
		invoke(sendAction);
	}

	@Override
	public Integer getSequence() {
		return sequence;
	}

	@Override
	public Object getDelegate() {
		return null;
	}

	@Override
	public boolean isConnected() {
		try {
			return dbConnection != null && dbConnection.isValid(10000);
		} catch (AbstractMethodError abstractMethodError) {
			return true;
		} catch (SQLException e) {
			throw new OpenLegacyRuntimeException(e);
		}
	}

	@Override
	public void disconnect() {
		try {
			if (dbConnection != null) {
				dbConnection.close();
			}
		} catch (SQLException e) {
		}
		dbConnection = null;
	}

	int java2SqlType(Class<?> jt) {
		int sqlType = Types.OTHER;
		if (jt.isAssignableFrom(String.class)) {
			sqlType = Types.VARCHAR;
		} else if (jt.isAssignableFrom(java.math.BigDecimal.class)) {
			sqlType = Types.NUMERIC;
		} else if (jt.isAssignableFrom(boolean.class)) {
			sqlType = Types.BIT;
			// } else if (jt.isAssignableFrom(boolean.class)) {
			// sqlType = Types.BOOLEAN;
		} else if (jt.isAssignableFrom(byte.class)) {
			sqlType = Types.TINYINT;
		} else if (jt.isAssignableFrom(short.class)) {
			sqlType = Types.SMALLINT;
		} else if (jt.isAssignableFrom(int.class)) {
			sqlType = Types.INTEGER;
		} else if (jt.isAssignableFrom(long.class)) {
			sqlType = Types.BIGINT;
		} else if (jt.isAssignableFrom(float.class)) {
			sqlType = Types.REAL;
			// } else if (jt.isAssignableFrom(double.class)) {
			// sqlType = Types.FLOAT;
		} else if (jt.isAssignableFrom(double.class)) {
			sqlType = Types.DOUBLE;
			// } else if (jt.isAssignableFrom(byte[].class)) {
			// sqlType = Types.BINARY;
		} else if (jt.isAssignableFrom(byte[].class)) {
			sqlType = Types.VARBINARY;
			// } else if (jt.isAssignableFrom(byte[].class)) {
			// sqlType = Types.LONGVARBINARY;
		} else if (jt.isAssignableFrom(java.sql.Date.class)) {
			sqlType = Types.DATE;
		} else if (jt.isAssignableFrom(java.sql.Time.class)) {
			sqlType = Types.TIME;
		} else if (jt.isAssignableFrom(java.sql.Timestamp.class)) {
			sqlType = Types.TIMESTAMP;
		}

		return sqlType;

	}

	@Override
	public RpcResult invoke(RpcInvokeAction rpcInvokeAction) {
		String procName = rpcInvokeAction.getRpcPath();

		List<RpcField> inputFields = new ArrayList<RpcField>();
		List<RpcField> outputFields = new ArrayList<RpcField>();

		List<RpcField> resultsFields = new ArrayList<RpcField>();

		for (RpcField f : rpcInvokeAction.getFields()) {
			if (f instanceof RpcFlatField) {
				if (f.getDirection() != Direction.NONE) {
					inputFields.add(f);
				}

				if (f.getDirection() == Direction.INPUT_OUTPUT || f.getDirection() == Direction.OUTPUT) {
					outputFields.add(f);
				}
			}

			if (f instanceof RpcStructureField) {
				if (f.getName().equals("results") || f.getDirection() == Direction.OUTPUT) {
					if (((RpcStructureField) f).getChildrens().size() > 0) {
						RpcField ff = ((RpcStructureField) f).getChildrens().get(0);
						if (ff instanceof RpcStructureListField) {
							resultsFields.add(ff);
						} else {
							resultsFields.add(f);
						}
					}
				}
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("{call ").append(procName).append("(");

		if (inputFields.size() > 0) {
			sb.append("?");

			for (int i = 1; i < inputFields.size(); ++i) {
				sb.append(", ?");
			}
		}

		sb.append(")}");

		try {
			CallableStatement cs = dbConnection.prepareCall(sb.toString());

			for (int i = 0; i < inputFields.size(); ++i) {
				RpcFlatField field = (RpcFlatField) inputFields.get(i);
				if (field.getDirection() == Direction.INPUT || field.getDirection() == Direction.INPUT_OUTPUT) {
					cs.setObject(field.getOrder() + 1, field.getValue());
				}

				if (field.getDirection() == Direction.INPUT_OUTPUT || field.getDirection() == Direction.OUTPUT) {
					cs.registerOutParameter(field.getOrder() + 1, java2SqlType(field.getValue().getClass()));
				}
			}

			cs.execute();

			// fetch values for output params

			for (RpcField f : outputFields) {
				((RpcFlatField) f).setValue(cs.getObject(f.getOrder() + 1));
			}

			// fetch result set

			for (RpcField resultField : resultsFields) {
				if (resultField instanceof RpcStructureListField) {
					SimpleRpcStructureListField lf = (SimpleRpcStructureListField) resultField;
					lf.getChildrens().clear();
				}
			}

			ResultSet rs = cs.getResultSet();

			if (rs != null) {
				boolean firstRecord = true;
				try {
					while (rs.next()) {
						for (RpcField resultField : resultsFields) {
							if (resultField instanceof RpcStructureListField) {
								SimpleRpcStructureListField lf = (SimpleRpcStructureListField) resultField;

								SimpleRpcFields ffs = new SimpleRpcFields();

								ResultSetMetaData md = rs.getMetaData();
								for (int i = 1; i <= md.getColumnCount(); ++i) {
									ffs.add(FieldsUtils.makeField(md.getColumnLabel(i), rs.getObject(i)));
								}

								lf.getChildrens().add(ffs);
							} else if (firstRecord && (resultField instanceof RpcStructureField)) {
								ResultSetMetaData md = rs.getMetaData();

								for (RpcField f : ((RpcStructureField) resultField).getChildrens()) {
									for (int i = 1; i <= md.getColumnCount(); ++i) {
										if (md.getColumnLabel(i).equalsIgnoreCase(f.getName())) {
											((SimpleRpcFlatField) f).setValue(rs.getObject(i));
										}
									}
								}

							}
						}

						firstRecord = false;
					}
				} catch (SQLException e) {
					// TODO: handle exception
				}

			}

		} catch (SQLException e) {
			throw new OpenLegacyRuntimeException(e);
		}

		SimpleRpcResult rpcResult = new SimpleRpcResult();
		rpcResult.setRpcFields(rpcInvokeAction.getFields());

		sequence++;
		return rpcResult;
	}

	@Override
	public void login(String user, String password) {
		try {
			Class.forName(dbDriverClassName);
			dbConnection = DriverManager.getConnection(dbUrl, user, password);
		} catch (SQLException e) {
			throw new OpenLegacyRuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new OpenLegacyRuntimeException(e);
		}

	}

}
