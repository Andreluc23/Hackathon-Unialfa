import { Router } from 'express';
import knex from './../database/knex/index';
import PDFDocument from 'pdfkit';

const routes = Router();

routes.get('/:id', async (req, res) => {
  const alunoId = req.params.id;

  try {
    const aluno = await knex('aluno')
      .join('evento', 'aluno.eve_id', 'evento.eve_id')
      .leftJoin('palestrante', 'evento.eve_id', 'palestrante.eve_id')
      .where('aluno.aluno_id', alunoId)
      .select(
        'aluno.nome as aluno_nome',
        'aluno.matricula_ra',
        'evento.nome as evento_nome',
        'palestrante.nome as palestrante_nome',
        'palestrante.descricao as palestrante_descr'
      )
      .first();

    if (!aluno) {
      res.status(404).json({ mensagem: 'Aluno não encontrado!' });
      return;
    }

    const doc = new PDFDocument();
    const chunks: Buffer[] = [];

    doc.on('data', (chunk) => chunks.push(chunk));
    doc.on('end', () => {
      const resultado = Buffer.concat(chunks);
      res.setHeader('Content-Type', 'application/pdf');
      res.setHeader('Content-Disposition', 'inline; filename=certificado.pdf');
      res.send(resultado);
    });

    doc.fontSize(25).text('CERTIFICADO DE PARTICIPAÇÃO', { align: 'center' });
    doc.moveDown();
    doc.fontSize(16).text(`Certificamos que ${aluno.aluno_nome} (RA: ${aluno.matricula_ra})`);
    doc.text(`participou do evento "${aluno.evento_nome}",`);
    doc.text(`com palestra ministrada por ${aluno.palestrante_nome}.`);
    doc.moveDown();
    doc.fontSize(14).text(`Tema: ${aluno.palestrante_descr}`, { align: 'justify' });
    doc.moveDown();
    doc.text('Atenciosamente, Coordenação Acadêmica - UniALFA', { align: 'right' });

    doc.end();
  } catch (error) {
    console.error(error);
    res.status(500).json({ mensagem: 'Erro ao gerar o certificado' });
  }
});

export default routes;
