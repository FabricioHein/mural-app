# Use the official Node.js runtime as a parent image
FROM node:14

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.json to the container
COPY package*.json ./

# Install application dependencies
RUN npm install -f

# Copy the rest of the application code to the container
COPY . .

# Expose the port for the Vue.js application to listen on
EXPOSE 8080

# Command to start the application
CMD ["npm", "run", "serve"]
