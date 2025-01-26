import type { FetchOptions } from "ohmyfetch";
import type { APICall } from "~/plugins/api";
import { buildQueryString } from "~/utils/functions/buildQueryString";

const utenti = (apiCall: APICall, opts: FetchOptions = {}) => {
  return {
    getUtenti: () => apiCall<any>(`/api/utenti`, "get", opts),
  };
};

export { utenti };
