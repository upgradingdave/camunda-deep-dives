[![Community Extension](https://img.shields.io/badge/Community%20Extension-An%20open%20source%20community%20maintained%20project-FF4700)](https://github.com/camunda-community-hub/community)
![Compatible with: Camunda Platform 8](https://img.shields.io/badge/Compatible%20with-Camunda%20Platform%208-0072Ce)
[![](https://img.shields.io/badge/Lifecycle-Incubating-blue)](https://github.com/Camunda-Community-Hub/community/blob/main/extension-lifecycle.md#incubating-)

# Simple NodeJs Zeebe Client

Be sure to update `.env` with valid domain name, zeebe client id, and secret. 

Then, test with zbctl: 

```shell
npm i g zbctl@8.3.0
. ./.env
zbctl status
```

Then, test with nodejs by running [test.js](test.js): 

```shell
npm test
```

