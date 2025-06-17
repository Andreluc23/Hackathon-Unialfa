import { Router } from 'express'
import knex from './../database/knex'

const routes = Router();

// Nova rota para buscar palestrante por evento
routes.get('/', async (req, res) => {
  try {
    const palestrantes = await knex('palestrante').select('*');
    res.json(palestrantes);
  } catch (error) {
    res.status(500).json({ mensagem: 'Erro ao listar palestrantes' });
  }
});


export default routes;