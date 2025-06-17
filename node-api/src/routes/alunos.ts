import { Router } from 'express'
import knex from './../database/knex/index'
import { z } from 'zod'

const routes = Router();

routes.get('/', async (req, res) => {
  try {
    const aluno = await knex('aluno').select('*');
    res.json(aluno);
  } catch (error) {
    res.status(500).json({ mensagem: 'Erro ao buscar alunos' });
  }
});

routes.get('/eventos-com-alunos', async (req, res) => {
  try {

    const evento = await knex('evento').select('*');

    
    const aluno = await knex('aluno')
      .select('nome', 'matricula_ra', 'eve_id');

    
    const eventosComAlunos = evento.map(evento => {
      return {
        ...evento,
        aluno: aluno.filter(aluno => aluno.eve_id === evento.eve_id)
      };
    });

    res.json(eventosComAlunos);

  } catch (error) {
    console.error(error);
    res.status(500).json({ mensagem: 'Erro ao buscar eventos com alunos' });
  }
});



routes.post('/', async (req, res) => {
  const registerBodySchema = z.object({
    nome: z.string(),
    email: z.string().email(),
    matricula_ra: z.number().min(2),
    eve_id: z.number() 
  });

  try {
    const { nome, email, matricula_ra, eve_id } = registerBodySchema.parse(req.body);

    // Correção aqui: trocado 'id' por 'eve_id'
    const eventoExiste = await knex('evento').where({ eve_id: eve_id }).first();
    if (!eventoExiste) {
       res.status(400).json({ mensagem: 'Evento não encontrado' });
       return;
    }

    await knex('aluno').insert({ nome, email, matricula_ra, eve_id });
    res.status(201).json({ mensagem: 'Aluno cadastrado com sucesso!' });
  } catch (error) {
    console.error(error);
    res.status(500).json({ mensagem: 'Erro ao cadastrar aluno' });
  }
});
export default routes
