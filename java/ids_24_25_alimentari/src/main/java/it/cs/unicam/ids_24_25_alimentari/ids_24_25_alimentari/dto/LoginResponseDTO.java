package it.cs.unicam.ids_24_25_alimentari.ids_24_25_alimentari.dto;

    public class LoginResponseDTO {

        private String token;
        private long scadenza;

        public String getToken() {
            return token;
        }

        public LoginResponseDTO setToken(String token) {
            this.token = token;
            return this;
        }

        public long getExpiresIn() {
            return scadenza;
        }

        public LoginResponseDTO setExpiresIn(long expiresIn) {
            this.scadenza = expiresIn;
            return this;
        }

        @Override
        public String toString() {
            return (
                    "LoginResponseDto{" +
                            "token='" +
                            token +
                            '\'' +
                            ", scadenza=" +
                            scadenza +
                            '}'
            );
        }
    }

