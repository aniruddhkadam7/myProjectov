package com.raybiztech.serverLog.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;

import com.raybiztech.date.Date;

@Service("deleteFilesService")
public class DeleteFilesService extends QuartzJobBean {

	DeleteFiles deleteFiles;

	public DeleteFiles getDeleteFiles() {
		return deleteFiles;
	}

	public void setDeleteFiles(DeleteFiles deleteFiles) {
		this.deleteFiles = deleteFiles;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
System.out.println("delete"+deleteFiles);
		//deleteFiles.deleteFile();
	}


}
