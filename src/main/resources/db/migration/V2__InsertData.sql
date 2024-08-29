-- справочник производителей (марок) авто
insert into vin_make (id, name, createdon, updatedon) values
    (460, 'Ford', null, null),
    (480, 'Infiniti', null, null),
    (483, 'Jeep', null, null),
    (496, 'Ram', null, null);

-- справочник моделей авто
insert into vin_model (id, name, createdon, updatedon) values
    (1684, 'V8 Vantage', null, null),
    (1800, 'Explorer', null, null),
    (1943, 'Wrangler', null, null),
    (2324, 'FX35', null, null),
    (13621, '2500', null, null);

-- связка производитель (марка) авто с моделями
insert into vin_make_model (id, makeid, modelid, createdon, updatedon) values
    (1, 440, 1684, null, null),
    (117, 460, 1800, null, null),
    (260, 483, 1943, null, null),
    (641, 480, 2324, null, null),
    (11934, 496, 13621, null, null);
