-- Dodaj przystanki
INSERT INTO Przystanek (Nazwa, Ulica)
VALUES 
  ('Grunwaldzki', 'Grunwaldzka'),
  ('Dworzec', 'Piłsudskiego'),
  ('Stadion', 'Krzycka'),
  ('Plac Bema', 'Sienkiewicza'),
  ('Rynek', 'Kazimierza Wielkiego'),
  ('Borek', 'Hallera');

-- Dodaj trasy
INSERT INTO Trasa (NazwaTrasy, NumerTrasy, Kierunek)
VALUES 
  ('Trasa Centrum', '101', 1),
  ('Trasa Północna', '202', 1);

-- Przypisz przystanki do trasy 101 (ID trasy = 1)
INSERT INTO PrzystanekNaTrasie (PrzystanekID, TrasaID, Kolejnosc, CzasPrzejazdu)
VALUES 
  (1, 1, 1, 0),     -- Grunwaldzki
  (2, 1, 2, 10),    -- Dworzec
  (5, 1, 3, 18),    -- Rynek (wspólny przystanek)
  (3, 1, 4, 25);    -- Stadion

-- Przypisz przystanki do trasy 202 (ID trasy = 2)
INSERT INTO PrzystanekNaTrasie (PrzystanekID, TrasaID, Kolejnosc, CzasPrzejazdu)
VALUES 
  (4, 2, 1, 0),     -- Plac Bema
  (5, 2, 2, 7),     -- Rynek (wspólny przystanek)
  (6, 2, 3, 15);    -- Borek

-- Harmonogramy dla trasy 101
INSERT INTO Kurs (GodzinaStartu, TrasaID)
VALUES 
  ('07:30:00', 1),
  ('08:00:00', 1);

-- Harmonogramy dla trasy 202
INSERT INTO Kurs (GodzinaStartu, TrasaID)
VALUES 
  ('06:45:00', 2),
  ('09:15:00', 2);

