package com.smartgarden.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ScriptRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScriptRunner.class);

	@ConfigProperty(name = "script.directory", defaultValue = "")
	String scriptDir;

	@ConfigProperty(name = "python.script.runner", defaultValue = "run-python.sh")
	String pythonRunner;

	public String executePythonScript(String scriptName, String parameter) {

		ProcessBuilder processBuilder = new ProcessBuilder();
		String script = resolveScriptPath(scriptName);
		processBuilder.environment().put("SCRIPT", script);
		if (parameter != null) {
			processBuilder.command("bash", pythonRunner, parameter);
		} else {
			processBuilder.command("bash", pythonRunner);
		}
		return runProcess(processBuilder);
	}

	public String executeScript(String scriptType, String scriptName, String parameter) {

		ProcessBuilder processBuilder = new ProcessBuilder();
		String script = resolveScriptPath(scriptName);
		processBuilder.environment().put("SCRIPT", script);
		if (parameter != null) {
			processBuilder.command(scriptType, script, parameter);
		} else {
			processBuilder.command(scriptType, script);
		}
	    return runProcess(processBuilder);
	}

	private String runProcess(ProcessBuilder processBuilder) {

		processBuilder.redirectErrorStream(true);
	    Process process = null;
	    List<String> results = null;

		try {
			process = processBuilder.start();
			results = readProcessOutput(process.getInputStream());
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}

	    if(results != null && results.size() > 0) {
	    	String resultString = String.join("; ", results);
	    	LOGGER.info("results: " + resultString);
	    	return resultString;
	    } else {
	    	return null;
	    }
	}

	private List<String> readProcessOutput(InputStream inputStream) throws IOException {
        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
            return output.lines().collect(Collectors.toList());
        }
    }

	private String resolveScriptPath(String filename) {
		return scriptDir + filename;
    }
}