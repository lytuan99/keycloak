module.exports = {
  purge: ["../**/*.ftl"],
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {
      colors: {
        primary: '#FC6634',
        secondary: {
          100: 'rgba(252, 102, 52, 0.65);',
          200: '#FE506A'
        },
        gray: {
          0: "#F8F8F8",
        },
        blackDark: "#242424",
        dark: "#4B4B4B",
      },
      borderRadius: {
        5: '5px',
      },
      fontFamily: {
        'sfd': 'SanFranciscoDisplay',
        'sfd-heavy': 'SanFranciscoDisplay-Heavy',
      }
    },
  },
  variants: {
    extend: {
    },
  },
  plugins: [],
}
