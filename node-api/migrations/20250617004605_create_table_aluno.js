/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function (knex) {
  return knex.schema.createTable("aluno", (table) => {
    table.increments("aluno_id").primary();
    table.string("nome").notNullable();
    table.string("email").notNullable();
    table.integer("matricula_ra").notNullable(); 
    table.string("password").notNullable(); 
    table.integer('eve_id').unsigned().notNullable() .references('eve_id').inTable('evento'); 
  });
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = function (knex) {
  return knex.schema.dropTable("aluno");
};