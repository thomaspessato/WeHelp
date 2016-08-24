# WeHelp #

App to help people interact with good actions

# Começando agora? Tá perdido? #

Utilizamos GIT para o versionamento do nosso código. Não sabe usar ainda? Entra aqui, que tá bem explicado!


## Para pegar o projeto no seu computador: ##

1. Ter o Git instalado na sua máquina.
2. Criar um FORK do projeto "WeHelp".
3. Dar um 'git clone' do SEU FORK.

A partir deste momento, para publicar qualquer alteração feita será necessário:

git add -A
git commit -m "nome do commit"
git push origin <nome do branch>

## Como funciona o processo de desenvolvimento e publicação? ##

Exemplo:

1. Pegar uma feature / issue para resolver (vinda do Trello).
2. Faz 'git pull origin master', para atualizar com as últimas modificações do projeto.
3. Trabalha para resolver AQUELA feature / issue.
4. Após resolver, faça um 'git add -A', para adicionar todos os arquivos alterados para o log de modificação.
5. Faça um 'git commit -m "nome da feature / issue"'.
6. Faça 'git pull origin master' e 'git pull origin sprint' para verificar se não há alguma modificação no projeto.


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

Google Android Training - Development

## Code review ##

Todos serão responsáveis por averiguar o código. Thomas Pessato e Israel Jardim serão responsáveis por 'aceitar' os PULL REQUEST, mas apenas se obedecer o critério de 70% dos envolvidos darem "Approve" no mesmo.

Desta forma, todos os envolvidos entenderam de alguma forma o que está sendo modificado, reduzindo a chance de gerarmos algum tipo de bug por falta de análise.

