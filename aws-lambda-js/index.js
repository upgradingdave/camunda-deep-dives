const { ZBClient } = require('zeebe-node')

exports.handler = async function(event) {
  console.log("EVENT: %s", JSON.stringify(event, null, 2));
  const zbc = new ZBClient();

  const result = await zbc.createProcessInstance('Process_tripBooking', {
    testData: 'something',
  })

  console.log(result)
}

