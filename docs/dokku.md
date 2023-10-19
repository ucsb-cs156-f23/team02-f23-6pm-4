
# Deploying on Dokku

Deploying on Dokku is straightforward: you only need these steps:

On your dokku server (once per team; the dokku server is shared by
the entire team):

1. Create the app: 
   ```
   dokku apps:create team02
   ```
   
2. Sync with repo (substitute your own team name):
   ```
   dokku git:sync team02 https://github.com/ucsb-cs156-f23/team02-f23-6pm-4 main
   ```

   We always deploy the `main` branch only on the `team02` deployment, which we consider our "production" deployment (or "prod").  When working in a team environment, it is typical
   to make sure that no code goes into the `main` branch except
   by a code reviewed pull request.

3. Build app:
   ```
   dokku ps:rebuild team02
   ```

Any time you need to redeploy, you can do so by repeating steps 2 and 3.

# Deploying a dev instance on Dokku

You can also create a private dev instance on Dokku
where you can try out your changes.  That allows you to 
deploy branches other than `main` and see what happens.

1. Create the app: 
   ```
   dokku apps:create team02-yourName
   ```
2. Sync with repo (substitute your own team name and branch name):
   ```
   dokku git:sync https://github.com/ucsb-cs156-f23/team02-f23-6pm-4 team02-yourName your-branch-name
   ```
3. Build app:
   ```
   dokku ps:rebuild team02-yourName
   ```

Any time you need to redeploy, you can do so by repeating steps 2 and 3.

# Do we need to configure HTTPS, OAuth, Databases, etc.

Most of the repos we deploy on Dokku in this course have both
a frontend, and a backend. In those cases it is necessary to define a configuration variable `PRODUCTION=true` which signals the `mvn spring-boot:run` command to launch both backend and 
frontend.

However, this app has only a backend, therefore it is not necessary
to do that.

This app doesn't use OAuth logins; it isn't necessary to authenticate since we aren't storing anything in a database.

Therefore, configuring HTTPS is optional. 

Similarly, we don't use a database, so there's no need to configure
a database connection in this app.
