## Descripció de la base de dades `comandes`

La taula `comandes` està dissenyada per gestionar les comandes del Bar Retina. Cada registre representa una comanda realitzada per un client i conté la següent informació:

- **`id_comanda`**: Identificador únic de la comanda, útil per modificar i fer el seguiment de les comandes.
- **`id_taula`**: Identificador de la taula que ha fet la comanda.
- **`id_cambrer`**: Identificador del cambrer assignat a la comanda.
- **`comanda`**: Text amb els detalls de la comanda.
- **`data_comanda`**: Data i hora en què es va realitzar la comanda.
- **`estat_comanda`**: Estat actual de la comanda.
- **`preu_comanda`**: Import total de la comanda.
- **`pagada`**: Indica si la comanda ha sigut pagada.

### Ús principal
Aquesta taula es fa servir per gestionar i fer un seguiment de les comandes d’un establiment, permetent controlar els detalls de cada comanda, el seu estat i la seva situació de pagament. Ideal per aplicacions de gestió de restaurants o bars.