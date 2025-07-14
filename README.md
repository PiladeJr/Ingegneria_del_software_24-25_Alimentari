# 🌿 IDS 24-25 Alimentari

![Java](https://img.shields.io/badge/Java-21-blue?logo=openjdk)

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-brightgreen?logo=spring)

![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?logo=gradle)

![H2 Database](https://img.shields.io/badge/Database-H2-lightgrey?logo=h2)

![Hibernate](https://img.shields.io/badge/Hibernate-JPA-orange?logo=hibernate)

![Spring Security](https://img.shields.io/badge/Spring%20Security-Enabled-green?logo=springsecurity)

![PayPal](https://img.shields.io/badge/PayPal-Integration-blue?logo=paypal)

![License](https://img.shields.io/badge/License-MIT-yellow)

## 📜 Descrizione del Progetto

Questo progetto di gruppo, nell'ambito dell'esame di Ingegneria del Software di Unicam, ci insegna come affrontare lo sviluppo di ogni artefatto software seguendo un Modello di Sviluppo Iterativo (il Processo Unificato). Il focus non è solo sullo sviluppo del codice, ma anche e soprattutto sul processo di analisi.

Il progetto si concentra sulla gestione di un marketplace completo dedicato alla vendita di prodotti alimentari tipici, prodotti trasformati e pacchetti esperienziali legati alle tradizioni locali.<br>

Coinvolge vari attori, inclusi produttori, trasformatori, distributori, animatori, curatori, gestori ognuno con ruoli specifici.

### ✨ Caratteristiche Principali:

- **🛍️ Gestione Prodotti**: Crea, modifica e gestisci prodotti individuali e pacchetti di prodotti

- **✅ Validazione Contenuti**: Sistema di revisione e approvazione da parte dei curatori per prodotti, informazioni aggiuntive ed eventi

- **🛒 Carrello della Spesa**: Gestione avanzata del carrello con prodotti multipli e quantità

- **📦 Gestione Ordini**: Ciclo di vita completo degli ordini dalla creazione alla consegna

- **💳 Integrazione Pagamenti**: Integrazione PayPal per transazioni sicure

- **👨‍⚖️ Gestione Utenti e Aziende**: Sistema multi-ruolo con associazioni aziendali

- **🎉 Gestione Eventi**: Organizza fiere, visite e degustazioni per promuovere prodotti locali

- **📄​ Gestione Informazioni**: Crea, modifica e gestisci informazioni personalizzate

- **🔐 Sicurezza**: Autenticazione basata su JWT e autorizzazione basata sui ruoli

- **📧 Servizi Email**: Notifiche email automatiche per vari eventi di sistema

---

## ⚙️ Configurazione e Installazione

### 📌 Prerequisiti

Assicurati di avere installato:

- **☕ JDK 21 o superiore**

- **🐘 Gradle 8.x**

### 🚀 Primi Passi

### 📥 Clona il Repository

```sh

$  git  clone  https://github.com/PiladeJr/Ingegneria_del_software_24-25_Alimentari.git

$  cd  Ingegneria_del_software_24-25_Alimentari/java/ids_24_25_alimentari

```

### 🛠️ Configura il Database

L'applicazione utilizza il database H2 in-memory di default. La configurazione si trova in `src/main/resources/application.properties`:

```properties

# Database H2 (in-memory per sviluppo)

spring.datasource.url=jdbc:h2:mem:testdb

spring.datasource.driverClassName=org.h2.Driver

spring.datasource.username=sa

spring.datasource.password=password



# Abilita Console H2 (accessibile da /h2-console)

spring.h2.console.enabled=true



# Impostazioni Hibernate

spring.jpa.hibernate.ddl-auto=update

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

```

### 🔧 Configura l'Integrazione PayPal

Crea un file `dev.properties` nella directory root con le tue credenziali PayPal:

```properties
paypal.client-id=${PAYPAL_CLIENT_ID:your-paypal-client-id}
paypal.client-secret=${PAYPAL_CLIENT_SECRET:your-paypal-secret}
spring.mail.username=${MAIL_USERNAME:your@email.com}
spring.mail.password=${MAIL_PASSWORD:your-email-password}

```

### 🏗️ Compila ed Esegui il Progetto

Usando Gradle wrapper:

```sh

# Compila il progetto

$  ./gradlew  build



# Esegui l'applicazione

$  ./gradlew  bootRun

```

### 🌍 Accesso all'Applicazione

Una volta avviata, l'applicazione sarà disponibile a:

```

Applicazione: http://localhost:8080

Console H2: http://localhost:8080/h2-console

```

## 📚 Documentazione API

L'applicazione fornisce API RESTful per varie funzionalità:

### 🛍️ API Prodotti

- `GET /api/prodotto/visualizza/all` - Ottieni tutti i prodotti (con ordinamento)

- `GET /api/prodotto/visualizza/id-azienda/{id}` - Ottieni prodotti per azienda

- `DELETE /api/prodotto/rimuovi-dallo-shop/{id}` - Rimuovi prodotto dal negozio

### 🛒 API Carrello

- `POST /api/carrello` - Crea carrello

- `PUT /api/carrello/{id}/aggiungi` - Aggiungi articoli al carrello

- `GET /api/carrello/utente/{userId}` - Ottieni carrello per utente

### 📦 API Ordini

- `GET /api/ordini` - Ottieni tutti gli ordini

- `POST /api/ordini` - Crea nuovo ordine

- `GET /api/ordini/{id}/stato` - Ottieni stato ordine con dettagli transazione

### 💳 API PayPal

- `POST /api/paypal/create-payment` - Crea pagamento PayPal

- `GET /api/paypal/success` - Gestisci pagamento riuscito

- `GET /api/paypal/cancel` - Gestisci pagamento annullato

### 👥 API Utenti

- `POST /api/utenti/register` - Registrazione utente

- `POST /api/utenti/login` - Autenticazione utente

## 🏗️ Architettura

### 📁 Struttura del Progetto

```

src/

├── main/

│ ├── java/it/cs/unicam/ids_24_25_alimentari/

│ │ ├── controllers/ # Controller REST

│ │ ├── modelli/ # Modelli Entità

│ │ │ ├── azienda/ # Entità azienda

│ │ │ ├── carrello/ # Entità carrello

│ │ │ ├── contenuto/ # Entità contenuto (prodotti, eventi)

│ │ │ ├── ordine/ # Entità ordine

│ │ │ ├── utente/ # Entità utente

│ │ │ └── transazione/ # Entità transazione

│ │ ├── servizi/ # Servizi Logica di Business

│ │ ├── repositories/ # Repository JPA

│ │ ├── dto/ # Oggetti di Trasferimento Dati

│ │ └── config/ # Classi di Configurazione

│ └── resources/

│ ├── application.properties

│ └── static/ # Risorse statiche (immagini, ecc.)

└── test/ # Test di Unità e Integrazione

```

### 🔧 Tecnologie Utilizzate

- **Spring Boot 3.5.0** - Framework principale

- **Spring Data JPA** - Persistenza dati

- **Spring Security** - Autenticazione e autorizzazione

- **Spring Web** - Servizi web RESTful

- **Spring Mail** - Servizi email

- **H2 Database** - Database in-memory per sviluppo

- **JWT** - JSON Web Tokens per autenticazione

- **PayPal SDK** - Elaborazione pagamenti

- **Lombok** - Riduzione codice boilerplate

- **Hibernate Validator** - Validazione dati

## 🗃️ Schema Database

### Entità Principali:

- **👤 Utente (User)**: Sistema utenti multi-ruolo (Cliente, Produttore, Moderatore, ecc.)

- **🏢 Azienda (Company)**: Entità aziendali con prodotti e collaborazioni

- **🛍️ Prodotto (Product)**: Prodotti individuali e pacchetti

- **🛒 Carrello (Cart)**: Carrello della spesa con articoli multipli

- **📦 Ordine (Order)**: Gestione ordini con tracciamento stato

- **💳 Transazione (Transaction)**: Record transazioni di pagamento

- **🎉 Evento (Event)**: Fiere e visite per promozione prodotti

## 🔐 Funzionalità di Sicurezza

- **Autenticazione JWT**: Autenticazione sicura basata su token

- **Autorizzazione basata sui Ruoli**: Diversi livelli di accesso per diversi tipi di utenti

- **Crittografia Password**: Hashing password BCrypt

- **Validazione Richieste**: Validazione input con Bean Validation

## 💻 Sviluppo

### 🧪 Esecuzione Test

```sh

$  ./gradlew  test

```

### 📊 Qualità del Codice

Il progetto segue le best practices Java e include:

- Architettura a livelli di servizio

- Pattern Repository con JPA

- Pattern DTO per comunicazione API

- Pattern Builder per creazione oggetti complessi

- Pattern Strategy per validazione contenuti

---

## 👥 Membri del Team

- **Federico Mengascini**

- **Pilade Jr Tomassini**

- **Davide Di Lorenzo**

## 📜 Licenza

Questo progetto è concesso in licenza sotto la Licenza MIT.
