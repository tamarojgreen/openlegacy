package org.openlegacy.providers.jt400.mockup;

import org.openlegacy.annotations.rpc.Action;
import org.openlegacy.annotations.rpc.RpcActions;
import org.openlegacy.annotations.rpc.RpcBooleanField;
import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcPart;

import java.util.Date;

@RpcEntity
@RpcActions(actions = { @Action(action = org.openlegacy.rpc.RpcActions.READ.class, path = "/QSYS.lib/QSYRUSRI.pgm") })
public class Qsyrusri implements org.openlegacy.rpc.RpcEntity {

	private UserInformation userInformation = new UserInformation();

	@RpcField(length = 4)
	private int recieverLength;

	@RpcField(length = 8, defaultValue = "USRI0100")
	private String format;

	@RpcField(length = 10, defaultValue = "CURRENT")
	private String profileName;

	@RpcField(length = 4, defaultValue = "0")
	private String errorCode;

	@RpcPart
	public static class UserInformation {

		@RpcField(length = 4)
		private Integer bytesReturned;

		@RpcField(length = 4)
		private Integer bytesAvailable;

		@RpcField(length = 10)
		private String userProfile;

		// @RpcDateField(pattern = "YYYMMDD")
		@RpcField(length = 7)
		private Date previousSignonDate;

		// @RpcDateField(pattern = "YYYMMDD")
		@RpcField(length = 6)
		private Date previousSignonTime;

		@RpcField(length = 1)
		private byte _;

		@RpcField(length = 4)
		private int badSignonAttempts;

		@RpcField(length = 10)
		private String status;

		@RpcField(length = 8)
		private byte[] passwordChangeDate;

		@RpcBooleanField(falseValue = "N", trueValue = "Y")
		@RpcField(length = 8)
		private Boolean noPassword;

		@RpcField(length = 4)
		private int passwordExpirationInterval;

		@RpcField(length = 8)
		private Date datePasswordExpired;

		@RpcField(length = 4)
		private Integer daysUntilPasswordExpires;

		@RpcField(length = 1)
		private String setPasswordToExpire;

		@RpcField(length = 10)
		private String displaySignOnInfo;

		public Integer getBytesReturned() {
			return bytesReturned;
		}

		public void setBytesReturned(Integer bytesReturned) {
			this.bytesReturned = bytesReturned;
		}

		public Integer getBytesAvailable() {
			return bytesAvailable;
		}

		public void setBytesAvailable(Integer bytesAvailable) {
			this.bytesAvailable = bytesAvailable;
		}

		public String getUserProfile() {
			return userProfile;
		}

		public void setUserProfile(String userProfile) {
			this.userProfile = userProfile;
		}

		public Date getPreviousSignonDate() {
			return previousSignonDate;
		}

		public void setPreviousSignonDate(Date previousSignonDate) {
			this.previousSignonDate = previousSignonDate;
		}

		public Date getPreviousSignonTime() {
			return previousSignonTime;
		}

		public void setPreviousSignonTime(Date previousSignonTime) {
			this.previousSignonTime = previousSignonTime;
		}

		public byte get_() {
			return _;
		}

		public void set_(byte _) {
			this._ = _;
		}

		public int getBadSignonAttempts() {
			return badSignonAttempts;
		}

		public void setBadSignonAttempts(int badSignonAttempts) {
			this.badSignonAttempts = badSignonAttempts;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public byte[] getPasswordChangeDate() {
			return passwordChangeDate;
		}

		public void setPasswordChangeDate(byte[] passwordChangeDate) {
			this.passwordChangeDate = passwordChangeDate;
		}

		public Boolean getNoPassword() {
			return noPassword;
		}

		public void setNoPassword(Boolean noPassword) {
			this.noPassword = noPassword;
		}

		public int getPasswordExpirationInterval() {
			return passwordExpirationInterval;
		}

		public void setPasswordExpirationInterval(int passwordExpirationInterval) {
			this.passwordExpirationInterval = passwordExpirationInterval;
		}

		public Date getDatePasswordExpired() {
			return datePasswordExpired;
		}

		public void setDatePasswordExpired(Date datePasswordExpired) {
			this.datePasswordExpired = datePasswordExpired;
		}

		public Integer getDaysUntilPasswordExpires() {
			return daysUntilPasswordExpires;
		}

		public void setDaysUntilPasswordExpires(Integer daysUntilPasswordExpires) {
			this.daysUntilPasswordExpires = daysUntilPasswordExpires;
		}

		public String getSetPasswordToExpire() {
			return setPasswordToExpire;
		}

		public void setSetPasswordToExpire(String setPasswordToExpire) {
			this.setPasswordToExpire = setPasswordToExpire;
		}

		public String getDisplaySignOnInfo() {
			return displaySignOnInfo;
		}

		public void setDisplaySignOnInfo(String displaySignOnInfo) {
			this.displaySignOnInfo = displaySignOnInfo;
		}

	}

	public UserInformation getUserInformation() {
		return userInformation;
	}

	public void setUserInformation(UserInformation userInformation) {
		this.userInformation = userInformation;
	}

	public int getRecieverLength() {
		return recieverLength;
	}

	public void setRecieverLength(int recieverLength) {
		this.recieverLength = recieverLength;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
