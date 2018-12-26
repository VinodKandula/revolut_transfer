# revolut_transfer

application implements transfer logic from one account to another.

There are several preinstalled "Holder" with their "Accounts" 


application is registered on 8888 port

api:

GET localhost:8888/holders/ - get all holders

GET localhost:8888/holders/:email - get holder by email

POST localhost:8888/transfer/ - commit transfer

There are two types of transfers:
1) is from account to account with body
{ moneyAmount: '100', debitAccountNumber: '10101010', creditAccountNumber: '10101020' }

2) is from account to holder's email with body
{ moneyAmount: '100', debitAccountNumber: '10101010', creditHolderEmail: 'h2@gmail.com' }

In project you can useful test script for testing api and text file with examples of request

P.S. DI in application is extremly simple because of small count of classes so I decided to construct all dependencies in one module
Also there are to types of repositories one is based on JPA and H2 database and another is a simple inmemory cache.
You can swich between them by providing param to main as "db"
I tried to implement optimistic locking apporach.
