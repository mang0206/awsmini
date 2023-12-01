FROM node:20.9.0 as develop-stage
RUN mkdir /app
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .

FROM develop-stage as build-stage
RUN npm run build

# production stage
FROM nginx:stable-alpine as production-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]