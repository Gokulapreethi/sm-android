package com.cg.ftpprocessor;

public interface FtpListener {

	public void FTPUploaded(FTPBean bean);

	public void FTPUploaingFailed(FTPBean bean, String reason);

	public void Downloaded(FTPBean bean);

	public void DownloadFailure(FTPBean bean, String reason);

}
