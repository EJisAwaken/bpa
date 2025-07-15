#Installation des dependances de l'application
FROM node:18-alpine as base

# CrÃ©e un dossier de travail
WORKDIR /app

# Copie les fichiers du projet
COPY . .

# Installe Angular CLI globalement
RUN npm install -g @angular/cli@15.2.11

# Installe les dÃ©pendances du projet
RUN npm install


#Application en phase de developpement
FROM base as dev
# Expose le port utilisÃ© par Angular en mode dev
WORKDIR /app

EXPOSE 4201

CMD ["npm", "run", "start", "--", "--host", "0.0.0.0"]


#Build du frontend pour le deploiement
FROM base as buildProd

RUN npm run build -- --configuration production

#Application en phase de production ou en phase de test
FROM nginx:latest as prod

COPY --from=buildProd /app/dist/validation-bpa-front/ /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
# Lance le serveur Angular
CMD ["nginx", "-g", "daemon off;"]


#Build du frontend pour le deploiement
FROM base as buildTest

RUN npm run build -- --configuration=test

#Application en phase de production ou en phase de test
FROM nginx:latest as test

COPY --from=buildTest /app/dist/validation-bpa-front/ /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
# Lance le serveur Angular
CMD ["nginx", "-g", "daemon off;"]
