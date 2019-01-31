# --- !Ups

--- Adiciona a constraint na coluna setando a mesma para not null

ALTER TABLE licenciamento.contato ALTER COLUMN telefone SET NOT NULL;

# --- !Downs

--- Retira a constraint de not null da coluna

ALTER TABLE licenciamento.contato ALTER COLUMN telefone DROP NOT NULL;