/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function(knex) {
  return knex.schema.createTable("palestrante", (table) => {
    table.increments("palestrante_id").primary();
    table.string("nome").notNullable();
    table.string(5000)("foto_url").notNullable();
    table.string(1000)("descricao").notNullable();
    table.string("especializacao").notNullable;
    table.string("contato").notNullable();
    table.integer('eve_id').unsigned().notNullable()
           .references('eve_id').inTable('evento'); // Corrigido para evento_id
  });
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = function(knex) {
    return knex.schema.dropTable("palestrante");
};
