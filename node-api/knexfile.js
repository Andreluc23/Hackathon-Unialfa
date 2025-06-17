const path = require('path');

module.exports = {
  development: {
    client: 'mysql2',
    connection: {
      database: 'eventosunialfafinal',
      user: 'root',
      password: ''
    },
    pool: { min: 2, max: 10 },
    migrations: {
      tableName: 'knex_migrations',
      directory: './migrations'
    }
  }
};

// fazendo a conexão com o banco


