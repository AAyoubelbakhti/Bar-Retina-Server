use comandes;
CREATE TABLE comandes (
    id_comanda INT PRIMARY KEY AUTO_INCREMENT,
    id_taula INT NOT NULL,
    id_cambrer INT NOT NULL,
    comanda TEXT,
    data_comanda DATETIME NOT NULL,
    estat_comanda VARCHAR(20),
    preu_comanda DECIMAL(10, 2),
    pagada BOOLEAN DEFAULT FALSE
);
