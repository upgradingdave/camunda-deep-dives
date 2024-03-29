const ZB= require('zeebe-node')

void (async () => {
  const zbc = new ZB.ZBClient()
  const topology = await zbc.topology()
  console.log(JSON.stringify(topology, null, 2))
})()