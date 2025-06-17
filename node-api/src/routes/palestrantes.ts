import { Router } from 'express';
import knex from './../database/knex';

const routes = Router();

// Lista todos os palestrantes
routes.get('/', async (req, res) => {
  try {
    const palestrantes = await knex('palestrante').select('*');
    res.json(palestrantes);
  } catch (error) {
    res.status(500).json({ mensagem: 'Erro ao listar palestrantes' });
  }
});

// Busca palestrante por ID
routes.get('/evento/:id', async (req, res) => {
  const { id } = req.params;

  try {
    const palestrante = await knex('palestrante').where('palestrante_id', id).first();

    if (!palestrante) {
       res.status(404).json({ mensagem: 'Palestrante não encontrado' });
       return
    }

    res.json(palestrante);
  } catch (error) {
    res.status(500).json({ mensagem: 'Erro ao buscar palestrante por ID' });
  }
});

export default routes;
