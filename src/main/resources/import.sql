insert into localizacao (id, latitude, longitude, base, galaxia) values (1, 1.1, 1.1, 'base 1', 'galaxia 1');
insert into localizacao (id, latitude, longitude, base, galaxia) values (2, 2.2, 2.2, 'base 2', 'galaxia 1');
insert into localizacao (id, latitude, longitude, base, galaxia) values (3, 3.3, 3.3, 'base 3', 'galaxia 1');

insert into inventario (id, armas, municoes, aguas, comidas) values (1, 10, 10, 10, 10);
insert into inventario (id, armas, municoes, aguas, comidas) values (2, 20, 20, 20, 20);
insert into inventario (id, armas, municoes, aguas, comidas) values (3, 3, 3, 3, 3);

insert into rebelde (id, nome, nascimento, genero, inventario_id, localizacao_id) values (1, 'rebelde 1', '2001-01-01', 'M', 1, 1);
insert into rebelde (id, nome, nascimento, genero, inventario_id, localizacao_id) values (2, 'rebelde 2', '2002-01-01', 'F', 2, 2);
insert into rebelde (id, nome, nascimento, genero, inventario_id, localizacao_id) values (3, 'rebelde 3', '2003-01-01', 'F', 3, 3);