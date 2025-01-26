import { $fetch } from "ohmyfetch";
import { defineNuxtPlugin } from "#app";
import { useCookie } from "nuxt/app";
import { useRuntimeConfig } from "nuxt/app";
import type { FetchOptions, FetchResponse } from "ohmyfetch";
import { richieste_collaborazione } from "~/api/richieste_collaborazione";

export type APICall = <T>(
  url: string,
  method: string,
  options?: FetchOptions
) => Promise<T>;

const parseData = (response: FetchResponse<any>): any | null => {
  if (response.status === 302) {
    return navigateTo(response._data?.data?.url ?? "/", { external: true });
  }

  return response._data ?? null;
};

export default defineNuxtPlugin((nuxtApp) => {
  const config = useRuntimeConfig();
  const bearerTokenKey = "BEARER-TOKEN";
  const bearerToken = useCookie(bearerTokenKey);
  const userKey = "USER";
  const user = useCookie(userKey);
  const $toast = useToast();
  const tempToken = useCookie("tempToken");

  /**
   * The response to this function carries the XSRF token
   * we do not need to save it anywhere.
   */

  const getHeaders = (options?: FetchOptions): Headers => {
    let opts: Headers = new Headers({
      Accept: "application/json",
      "Cache-Control": "no-cache",
      ...options?.headers,
    });

    if (bearerToken.value) {
      opts.set("Authorization", `Bearer ${bearerToken.value}`);
    }

    return opts;
  };

  const getOptions = (
    options: FetchOptions = {},
    method: string = "post"
  ): FetchOptions<any> => {
    const headers = getHeaders(options);
    const { headers: discard, ...opts } = options;

    return {
      baseURL: process.server
        ? (config.public.SSR_API_BASE as string)
        : (config.public.API_BASE_URL as string),
      method: method,
      headers,
      ...opts,
    };
  };
  const apiCall: APICall = async (url, method, options) => {
    const apiOptions = getOptions(options, method);
    try {
      return await $fetch.raw(url, apiOptions).then(parseData);
    } catch (error: any) {
      // Gestisci l'errore 401 (Unauthorized)
      if (error.response?.status === 401) {
        // Reset del token e reindirizzamento al login
        api.resetAuth();
        $toast.add({
          id: "error_post_avatar",
          title: "Sessione scaduta",
          icon: "i-heroicons-x-circle",
          color: "red",
        });
        return navigateTo("/login");
      }
      // Rilancia altri errori per essere gestiti altrove
      throw error;
    }
  };

  const api = {
    apiCall,
    richieste_collaborazione: richieste_collaborazione(apiCall),
    hasBearer: () => !!bearerToken.value,
    resetAuth: () => {
      bearerToken.value = null;
    },
    setBearerToken: (jwt: string | null) => {
      bearerToken.value = jwt;
    },
    setUsername: (userValue: any | null) => {
      user.value = userValue;
    },
    setTempToken: (token: string | null) => {
      tempToken.value = token;
    },
    getUser: () => user.value as any,
  };

  return {
    provide: {
      api,
    },
  };
});
