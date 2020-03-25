package controllers;

import utils.JobUtil;

public class Jobs extends GenericController {

	public static void callByClassName(String className) throws Exception {

		renderText(JobUtil.callByClassName(className));

	}

	public static void callAll() throws Exception {

		renderText(JobUtil.callAll());

	}

}
