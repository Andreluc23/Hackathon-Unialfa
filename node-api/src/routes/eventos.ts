import { Router } from 'express'
import knex from './../database/knex/index'

const routes = Router();

routes.get('/', async (req, res) => {
  try {
    const evento = await knex('evento').select('*');
    res.json(evento);
  } catch (error) {
    res.status(500).json({ mensagem: 'Erro ao buscar eventos' });
  }
});

routes.get('/:id', async (req, res) => {
  const { id } = req.params;

  try {
    const evento = await knex('evento').where('eve_id', id).first();

    if (!evento) {
      res.status(404).json({ mensagem: 'Evento não encontrado' });
      return
    }

    res.json(evento);
  } catch (error) {
    res.status(500).json({ mensagem: 'Erro ao buscar evento por ID' });
  }
});



export default routes