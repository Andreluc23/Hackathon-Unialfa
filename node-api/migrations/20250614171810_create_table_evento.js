/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function (knex) {
    return knex.schema.createTable("evento", (table => {
        table.increments("eve_id").primary();
        table.string("nome").notNull();
        table.string("descricao").notNull()
        table.date("data_ini").notNull();
        table.date("fim").notNull();
        table.string("local").notNull();
        table.integer('palestrante_id').unsigned().notNullable().references('palestrante_id').inTable('palestrante');

    }))
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = function (knex) {
    return knex.schema.dropTable("evento");

};
