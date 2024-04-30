[![Community Extension](https://img.shields.io/badge/Community%20Extension-An%20open%20source%20community%20maintained%20project-FF4700)](https://github.com/camunda-community-hub/community)
![Compatible with: Camunda Platform 8](https://img.shields.io/badge/Compatible%20with-Camunda%20Platform%208-0072Ce)
[![](https://img.shields.io/badge/Lifecycle-Incubating-blue)](https://github.com/Camunda-Community-Hub/community/blob/main/extension-lifecycle.md#incubating-)

# A Java Zeebe Client

A very simple command line tool written in java for testing connections to zeebe

# Usage Examples

```shell
java -jar zbctl.jar \
  --address my.domain.com:443 \
  --authzUrl https://my.domain.com:443/auth/realms/camunda-platform/protocol/openid-connect/token \
  --clientId zeebe \
  --clientSecret MYSECRET \
  --certPath ./certs/pretendpear.cer
```




