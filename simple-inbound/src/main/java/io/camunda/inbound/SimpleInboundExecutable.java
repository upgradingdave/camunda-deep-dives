package io.camunda.inbound;

import io.camunda.connector.api.annotation.InboundConnector;
import io.camunda.connector.api.inbound.InboundConnectorContext;
import io.camunda.connector.api.inbound.InboundConnectorExecutable;
import io.camunda.connector.generator.dsl.BpmnType;
import io.camunda.connector.generator.java.annotation.ElementTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@InboundConnector(name = "Simple Inbound Connector", type = "io.camunda:simple-inbound:1")
@ElementTemplate(
    id = "io.camunda.connectors.simpleInbound",
    name = "Simple Connector",
    icon = "icon.svg",
    version = 6,
    inputDataClass = SimpleInboundProperties.class,
    description = "Demonstrate Inbound Connector",
    documentationRef =
        "https://docs.camunda.io/docs/components/connectors",
    propertyGroups = {
        @ElementTemplate.PropertyGroup(id = "settings", label = "Settings")
    },
    elementTypes = {
        @ElementTemplate.ConnectorElementType(
            appliesTo = BpmnType.START_EVENT,
            elementType = BpmnType.MESSAGE_START_EVENT,
            templateIdOverride = "io.camunda.connectors.simpleInboundStart.v1",
            templateNameOverride = "Simple Inbound Start Event Connector"),
        @ElementTemplate.ConnectorElementType(
            appliesTo = {BpmnType.INTERMEDIATE_THROW_EVENT, BpmnType.INTERMEDIATE_CATCH_EVENT},
            elementType = BpmnType.INTERMEDIATE_CATCH_EVENT,
            templateIdOverride = "io.camunda.connectors.simpleInboundIntermediate.v1",
            templateNameOverride = "Simple Inbound Intermediate Catch Event Connector"),
        @ElementTemplate.ConnectorElementType(
            appliesTo = BpmnType.BOUNDARY_EVENT,
            elementType = BpmnType.BOUNDARY_EVENT,
            templateIdOverride = "io.camunda.connectors.simpleInboundBoundary.v1",
            templateNameOverride = "Simple Inbound Boundary Event Connector")
    })
public class SimpleInboundExecutable implements InboundConnectorExecutable<InboundConnectorContext> {
  private static final Logger LOG = LoggerFactory.getLogger(SimpleInboundExecutable.class);

  @Override
  public void activate(InboundConnectorContext context) {
    LOG.info("Simple Inbound Connector activated");
  }

  public void deactivate() {
    LOG.info("Simple Inbound Connector deactivated");
  }

}

