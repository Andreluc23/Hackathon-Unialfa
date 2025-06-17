//o router vai organizar as rotas
import { Router } from 'express';

import alunos from './alunos';
import eventos from './eventos';
import palestrantes from './palestrantes';
import session from './session';
//import autenticacao from '../../middlewares/autenticacao';
import certificado from './certificado';

//cria o roteador principal onde tudo sera conectado
const routes = Router()

routes.use('/alunos', alunos); // routes.use('/alunos', autenticacao, alunos);
routes.use('/eventos', eventos);
routes.use('/session', session)
routes.use('/palestrantes', palestrantes);
routes.use('/certificado', certificado);
//routes.use(autenticacao)

export default routes