# WeHelp #

App to help people interact with good actions

# Começando agora com Git? #
[Instalação](https://git-scm.com/book/pt-br/v1/Primeiros-passos-Instalando-Git)
Não sabe usar direito ou não lembra bem? [Entra aqui](http://rogerdudler.github.io/git-guide/index.pt_BR.html), que tá bem explicado!

## Pull Request? Fork? Que isso? ##

Entra [aqui](http://tableless.com.br/contribuindo-em-projetos-open-source-com-o-github/) e dá uma lida. É mais simples do que parece.

## Para começar a trabalhar com o projeto no seu computador: ##

1. Ter o Git instalado na sua máquina.
2. Criar um [Fork](https://help.github.com/articles/fork-a-repo/) do projeto "WeHelp".
3. Dar um 'git clone' do SEU FORK. (no terminal)
4. Entrar na pasta que foi clonada (chamada WeHelp) e, no terminal, dar um "git remote add wehelp git@github.com:WeHelpApp/WeHelp.git"
A partir deste momento, sempre que for trabalhar em alguma coisa, é recomendado fazer "git pull wehelp master" e "git pull wehelp sprint", para verificar se há alguma modificação no projeto por alguma outra pessoa. Isto faz com que os arquivos alterados sejam 'mergeados' com os seus, criando uma versão mesclada e totalmente atualizada.

5. Após alterar os arquivos da pasta WeHelp com o que for necessário, se faz uso dos seguintes comandos:

git add -A
git commit -m "<nome para o commit>"
git push origin <nome do branch>

Exemplo de fluxo:

1. Pegar uma das tarefas, definidas no início do sprint.
1. 'git pull wehelp master' e 'git pull wehelp sprint', para atualizar com as últimas modificações do projeto.
2. Trabalha para resolver AQUELA feature / issue.
3. Após resolver, faça um 'git add -A', para adicionar todos os arquivos alterados para o log de modificação.
4. 'git commit -m "nome da feature / issue"'.
5. 'git pull wehelp master' e 'git pull wehelp sprint' para verificar se não há alguma modificação no projeto desde que começou a trabalhar.
6. Resolver conflitos de código (se houver) e realizar as etapas 3 e 4 novamente.
7. 'git push origin <nome do branch que está trabalhando>'
8. Ir no Github.com e abrir um Pull Request para "WeHelp/sprint".

E recomeça o ciclo, em 1.


## Divisão dos branches ##

A divisão dos branches, no repositório principal, será:

"master" -> versão estável do aplicativo
"preview" -> modificações que irão rapidamente para o "master"
"hlg" -> modificações do sprint centralizadas com conflitos de código resolvidos, prontas para homologar
"sprint" -> entregas de tarefas relacionadas ao sprint

### Por que usar um branch 'preview' e um branch 'review'? ###

O branch preview pode ser usado para qualquer publicação, seja ela relacionada ou não ao sprint.

Exemplo: aconteceu algum problema e encontramos um bug crítico que está no MASTER, mas foge do nosso SPRINT. Utilizamos o branch PREVIEW para enviarmos a correção emergencial, poluirmos nosso SPRINT com tarefas que não são relacionadas.


## O que se usa para desenvolver o aplicativo? ##

O aplicativo é feito usando a IDE Android Studio, na linguagem JAVA.
Não tem prática com Java para Android?

A Google tem uma documentação bem boa, na parte de Training:

[Google Android Training - Development](https://developer.android.com/training/index.html)

## Code review ##

Todos serão responsáveis por averiguar o código. Thomas Pessato e Israel Jardim serão responsáveis por 'aceitar' os PULL REQUEST, mas apenas se obedecer o critério de 70% dos envolvidos darem "Approve" no mesmo.

Desta forma, todos os envolvidos entenderão de alguma forma o que está sendo modificado, reduzindo a chance de gerarmos algum tipo de bug por falta de análise.

