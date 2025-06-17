import Router from 'express'
import knex from '../database/knex'
import { z } from 'zod'
import { hash, compare } from 'bcryptjs'
import { sign } from 'jsonwebtoken'
import alunos from './alunos'

const router = Router();

router.post('/', async (req, res) => {
    const registerBodySchema = z.object({
        nome: z.string(),
        email: z.string().email(),
        matricula_ra: z.number().min(2),
        password: z.string().min(6),
        eve_id: z.number()
    });

    try {
        const { nome, email, matricula_ra, password, eve_id } = registerBodySchema.parse(req.body);

        const eventoExiste = await knex('evento').where({ id: eve_id }).first();
        if (!eventoExiste) {
            res.status(400).json({ mensagem: 'Esse evento não foi encontrado' });
        }

        const senhaCriptografada = await hash(password, 10);

        await knex('aluno').insert({ nome, email, matricula_ra, password: senhaCriptografada, eve_id });

        res.status(201).json({ mensagem: 'Aluno cadastrado com sucesso!' });
    } catch (error) {
        console.error(error);
        res.status(500).json({ mensagem: 'Erro ao cadastrar aluno' });
    }
});

export default router