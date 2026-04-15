FROM node:lts-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 5173
# Run Vue dev server
CMD ["npm", "run", "dev", "--", "--host"]