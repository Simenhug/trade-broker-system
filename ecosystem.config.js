module.exports = {
  apps: [{
    name: 'trade-simulator',
    script: './index.js'
  }],
  deploy: {
    production: {
      user: 'ubuntu',
      host: 'ec2-54-172-230-51.compute-1.amazonaws.com',
      key: '/c/Users/simen/.ssh/trade_simulator/AWSfreet2microTradeSimulator.pem',
      ref: 'origin/master',
      repo: 'git@github.com:Simenhug/trade-broker-system.git',
      path: '/home/ubuntu/Codes/trade_simulator',
      'post-deploy': 'npm install && pm2 startOrRestart pm2.json'
    }
  }
}