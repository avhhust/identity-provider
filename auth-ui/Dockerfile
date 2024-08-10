FROM node:22-alpine

COPY package-lock.json .
COPY package.json .

RUN npm install

COPY . .

RUN npm run build 