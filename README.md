# captsone-nf-ordersystem
Welcome the capstone project. This program can be used as a web application for a order system in a company.
## Define variable:
In backend/src/main/resources/application.properties the environment variable MONGO_DB_CONNECTION_STRING needs to be defined. Set it as a connection string to a mongoDB of your choice.
## Login needs a User
Once you have established the connection to your MongoDB. Open your MongoDB manually with for example MongoCompas. Create a Database named CapstoneOrderSystemDB and a Collection UserList. Now create a new entry in this collection. Choose a username and password of your choice and add it to the created user. The password needs to be decoded in Argon2 encryption. You may use a JShell to create your encrypted password. Lastly add a empty list with name roles to the created user. Now you should be able to login with this user.
## Product List in Hub:
You need to add products to your mongodb before you can see any products. This can be done with the add product feature on the website.
