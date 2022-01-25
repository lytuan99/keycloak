// Snowpack Configuration File
// See all supported options: https://www.snowpack.dev/reference/configuration

/** @type {import("snowpack").SnowpackUserConfig } */
module.exports = {
  mount: {
    src : '/'
  },
  plugins: ['@snowpack/plugin-postcss'],
  devOptions: {
    tailwindConfig: './tailwind.config.js',
  },
  buildOptions: {
    out: 'css',
  },
  optimize: {
    bundle: true,
    entrypoints: ['./src/index.js'],
    minify: true,
    target: 'es2017',
  },
};
