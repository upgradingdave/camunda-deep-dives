A simple setup to run Camunda 8.8 with Elasticsearch 8.17.4 and Kibana 8.17.4 in Docker.

## Start Elastic in Docker

```shell
docker network create elastic
```

```shell
docker pull docker.elastic.co/elasticsearch/elasticsearch:8.17.4
docker run --name es01 --net elastic -p 9200:9200 -it -m 1GB docker.elastic.co/elasticsearch/elasticsearch:8.17.4
```

## Start Kibana

```shell
docker pull docker.elastic.co/kibana/kibana:8.17.4
docker run --name kib01 --net elastic -p 5601:5601 docker.elastic.co/kibana/kibana:8.17.4
```

# Start Camunda 8

```shell
docker pull docker.elastic.co/camunda/camunda:8.8.0-alpha3

docker run \
-e ZEEBE_BROKER_CLUSTER_NODEID=0 \
-e ZEEBE_BROKER_CLUSTER_PARTITIONSCOUNT=3 \
-e ZEEBE_BROKER_CLUSTER_REPLICATIONFACTOR=3 \
-e ZEEBE_BROKER_CLUSTER_CLUSTERSIZE=3 \
-e ZEEBE_BROKER_EXPORTERS_ELASTICSEARCH_ARGS_URL=https://es01:9200 \
-e ZEEBE_BROKER_EXPORTERS_ELASTICSEARCH_ARGS_AUTHENTICATION_USERNAME=camunda \
-e ZEEBE_BROKER_EXPORTERS_ELASTICSEARCH_ARGS_AUTHENTICATION_PASSWORD=camunda \
-e ZEEBE_BROKER_EXPORTERS_ELASTICSEARCH_CLASSNAME=io.camunda.zeebe.exporter.ElasticsearchExporter \
-e ZEEBE_BROKER_CLUSTER_INITIALCONTACTPOINTS="zeebe-0:26502,zeebe-1:26502,zeebe-2:26502" \
--net elastic \
--entrypoint "/usr/local/camunda/bin/broker" \
--name zeebe-0 \
camunda/camunda:8.8.0-alpha3
```

```shell
docker run \
-e ZEEBE_BROKER_CLUSTER_NODEID=1 \
-e ZEEBE_BROKER_CLUSTER_PARTITIONSCOUNT=3 \
-e ZEEBE_BROKER_CLUSTER_REPLICATIONFACTOR=3 \
-e ZEEBE_BROKER_CLUSTER_CLUSTERSIZE=3 \
-e ZEEBE_BROKER_EXPORTERS_ELASTICSEARCH_ARGS_URL=https://es01:9200 \
-e ZEEBE_BROKER_EXPORTERS_ELASTICSEARCH_ARGS_AUTHENTICATION_USERNAME=camunda \
-e ZEEBE_BROKER_EXPORTERS_ELASTICSEARCH_ARGS_AUTHENTICATION_PASSWORD=camunda \
-e ZEEBE_BROKER_EXPORTERS_ELASTICSEARCH_CLASSNAME=io.camunda.zeebe.exporter.ElasticsearchExporter \
-e ZEEBE_BROKER_CLUSTER_INITIALCONTACTPOINTS="zeebe-0:26502,zeebe-1:26502,zeebe-2:26502" \
--net elastic \
--entrypoint "/usr/local/camunda/bin/broker" \
--name zeebe-1 \
camunda/camunda:8.8.0-alpha3
```

```shell
docker run \
-e ZEEBE_BROKER_CLUSTER_NODEID=2 \
-e ZEEBE_BROKER_CLUSTER_PARTITIONSCOUNT=3 \
-e ZEEBE_BROKER_CLUSTER_REPLICATIONFACTOR=3 \
-e ZEEBE_BROKER_CLUSTER_CLUSTERSIZE=3 \
-e ZEEBE_BROKER_EXPORTERS_ELASTICSEARCH_ARGS_URL=https://es01:9200 \
-e ZEEBE_BROKER_EXPORTERS_ELASTICSEARCH_ARGS_AUTHENTICATION_USERNAME=camunda \
-e ZEEBE_BROKER_EXPORTERS_ELASTICSEARCH_ARGS_AUTHENTICATION_PASSWORD=camunda \
-e ZEEBE_BROKER_EXPORTERS_ELASTICSEARCH_CLASSNAME=io.camunda.zeebe.exporter.ElasticsearchExporter \
-e ZEEBE_BROKER_CLUSTER_INITIALCONTACTPOINTS="zeebe-0:26502,zeebe-1:26502,zeebe-2:26502" \
--net elastic \
--entrypoint "/usr/local/camunda/bin/camunda" \
--name zeebe-2 \
camunda/camunda:8.8.0-alpha3
```

```shell
docker run \
-e ZEEBE_GATEWAY_CLUSTER_INITIALCONTACTPOINTS="zeebe-0:26502,zeebe-1:26502,zeebe-2:26502" \
--net elastic \
--entrypoint "/usr/local/camunda/bin/gateway" \
--name zeebe-gateway \
-p 26500:26500 -p 8080:8080 \
camunda/camunda:8.8.0-alpha3
```