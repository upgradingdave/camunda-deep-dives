package org.example.camunda.process.solution.service;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.CompleteJobResponse;
import org.example.camunda.process.solution.config.ProcessSolutionConfiguration;
import org.example.camunda.process.solution.model.ProcessSolutionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ZeebeService {

  @Autowired private ZeebeClient zeebe;

  @Autowired private ProcessSolutionConfiguration config;

  public String parseFormIdFromKey(String formKey) {
    Pattern pattern = Pattern.compile("[^:]+:[^:]+:(.*)");
    Matcher matcher = pattern.matcher(formKey);
    while (matcher.find()) {
      return matcher.group(1);
    }
    return null;
  }

  public String getFormSchemaFromBpmn(String formId)
      throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = builderFactory.newDocumentBuilder();
    Document xmlDocument = builder.parse(config.getBpmnDefinitionResource().getInputStream());
    XPath xPath = XPathFactory.newInstance().newXPath();
    String expression = "//*[@id=\"" + formId + "\"]";
    NodeList nodeList =
        (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
    if (nodeList != null && nodeList.getLength() == 1) {
      return nodeList.item(0).getFirstChild().getNodeValue();
    } else {
      throw new IllegalStateException("Unable to find json schema for form with name " + formId);
    }
  }

  public ProcessSolutionResponse completeTask(long jobKey, Map<String, Object> variables) {
    CompleteJobResponse completeJobResponse =
        zeebe.newCompleteCommand(jobKey).variables(variables).send().join();
    Map<String, Object> controlVariables = new HashMap<>();
    controlVariables.put("jobKey", jobKey);
    return ProcessSolutionResponse.withResult("completeTask", variables, controlVariables);
  }

  public ProcessSolutionResponse startProcess(String processId, Map<String, Object> variables) {
    final var processInstanceResult =
        zeebe
            .newCreateInstanceCommand()
            .bpmnProcessId(processId)
            .latestVersion()
            .variables(variables)
            // .withResult()
            .send()
            .join();

    return ProcessSolutionResponse.withResult("startProcess", variables, processInstanceResult);
  }

  public void requestTimeout(long jobKey) {
    zeebe.newUpdateRetriesCommand(jobKey).retries(1).requestTimeout(Duration.ofMinutes(30));
  }
}
