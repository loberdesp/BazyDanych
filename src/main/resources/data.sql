-- Przystanki
INSERT INTO Przystanek (Nazwa, Ulica)
VALUES
  ('Leśnica', 'ul. Średzka'),
  ('Pilczyce', 'ul. Lotnicza'),
  ('Popowice', 'ul. Popowicka'),
  ('Plac Jana Pawła II', 'pl. Jana Pawła II'),
  ('Rynek', 'pl. Solny'),
  ('Dworzec Główny', 'ul. Piłsudskiego'),
  ('Plac Grunwaldzki', 'pl. Grunwaldzki'),
  ('Hala Stulecia', 'ul. Wystawowa'),
  ('Biskupin', 'ul. Olszewskiego'),
  ('Port Lotniczy', 'ul. Graniczna'),
  ('Klecina', 'ul. Wałbrzyska'),
  ('Borek', 'ul. Hallera'),
  ('Krzyki', 'ul. Powstańców Śląskich'),
  ('Gaj', 'ul. Bardzka'),
  ('Księże Małe', 'ul. Opolska'),
  ('Brodzki', 'ul. Brodzka'),
  ('Psie Pole', 'ul. Kiełczowska'),
  ('Sołtysowice', 'ul. Sołtysowicka'),
  ('Kromera', 'pl. Kromera'),
  ('Nowy Dwór', 'ul. Rogowska');



-- Trasy (rzeczywiste i zredukowane)
INSERT INTO Trasa (NazwaTrasy, NumerTrasy, Kierunek)
VALUES
  ('Księże Małe', 3, 0),
  ('Leśnica', 3, 0),
  ('Biskupin', 10, 0),
  ('Leśnica', 10, 0),
  ('Dworzec Główny', 106, 0),
  ('Port Lotniczy', 106, 0);

-- Przystanki na trasach
INSERT INTO PrzystanekNaTrasie (PrzystanekID, TrasaID, Kolejnosc, CzasPrzejazdu)
VALUES
  (1, 1, 1, 0),
  (2, 1, 2, 7),
  (3, 1, 3, 5),
  (4, 1, 4, 6),
  (5, 1, 5, 4),
  (6, 1, 6, 6),
  (13, 1, 7, 5),
  (14, 1, 8, 7),
  (15, 1, 9, 6),
  (15, 2, 1, 0),
  (14, 2, 2, 6),
  (13, 2, 3, 5),
  (6, 2, 4, 6),
  (5, 2, 5, 4),
  (4, 2, 6, 6),
  (3, 2, 7, 5),
  (2, 2, 8, 7),
  (1, 2, 9, 6),
  (1, 3, 1, 0),
  (2, 3, 2, 6),
  (3, 3, 3, 4),
  (4, 3, 4, 5),
  (5, 3, 5, 3),
  (6, 3, 6, 5),
  (7, 3, 7, 4),
  (8, 3, 8, 3),
  (9, 3, 9, 2),
  (9, 4, 1, 0),
  (8, 4, 2, 3),
  (7, 4, 3, 4),
  (6, 4, 4, 5),
  (5, 4, 5, 3),
  (4, 4, 6, 5),
  (3, 4, 7, 4),
  (2, 4, 8, 6),
  (1, 4, 9, 2),
  (10, 5, 1, 0),
  (20, 5, 2, 10),
  (2, 5, 3, 6),
  (6, 5, 4, 8),
  (6, 6, 1, 0),
  (2, 6, 2, 8),
  (20, 6, 3, 10),
  (10, 6, 4, 10);

-- Kursy
INSERT INTO Kurs (GodzinaStartu, TrasaID)
VALUES
  ('07:05:00', 1),
  ('09:05:00', 1),
  ('07:10:00', 2),
  ('09:10:00', 2),
  ('07:15:00', 3),
  ('09:15:00', 3),
  ('07:20:00', 4),
  ('09:20:00', 4),
  ('07:25:00', 5),
  ('09:25:00', 5),
  ('07:30:00', 6),
  ('09:30:00', 6);

