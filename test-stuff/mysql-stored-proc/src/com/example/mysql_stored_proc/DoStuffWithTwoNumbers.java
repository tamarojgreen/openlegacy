package com.example.mysql_stored_proc;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoStuffWithTwoNumbers {
	private int n1;
	private int n2;
	private long summ;
	private long sub;
	private long mul;

	public int getN1() {
		return this.n1;
	}

	public void setN1(int n1) {
		this.n1 = n1;
	}

	public int getN2() {
		return this.n2;
	}

	public void setN2(int n2) {
		this.n2 = n2;
	}

	public long getSumm() {
		return this.summ;
	}

	public void setSumm(long summ) {
		this.summ = summ;
	}

	public long getSub() {
		return this.sub;
	}

	public void setSub(long sub) {
		this.sub = sub;
	}

	public long getMul() {
		return this.mul;
	}

	public void setMul(long mul) {
		this.mul = mul;
	}

	public static DoStuffWithTwoNumbers invoke(DoStuffWithTwoNumbers in) {
		DoStuffWithTwoNumbers out = in;

		ResultSet resultSet = null;

		try {
			CallableStatement cs = DbUtil.getConnection().prepareCall(
					"{call doStuffWithTwoNumbers(?, ?)}");

			cs.setInt(1, in.getN1());
			cs.setInt(2, in.getN2());

			resultSet = cs.executeQuery();

			if (resultSet.next()) {
				out.setSumm(resultSet.getLong(1));
				out.setSub(resultSet.getLong(2));
				out.setMul(resultSet.getLong(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return out;
	}
}