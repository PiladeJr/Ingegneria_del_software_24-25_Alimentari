import type { FetchOptions } from "ohmyfetch";
import type { APICall } from "~/plugins/api";
import { buildMultiPartFormData } from "~/utils/functions/buildMultiPartFormData";

const richieste_collaborazione = (
  apiCall: APICall,
  opts: FetchOptions = {}
) => {
  return {
    getRichiesteCollaborazione: () =>
      apiCall<any>(`/api/richieste-collaborazione`, "get", opts),
    getRichiestaCollaborazione: (id: string) =>
      apiCall<any>(`/api/richieste-collaborazione/${id}`, "get", opts),
    createRichiestaCollaborazioneAzienda: (data: any) =>
      apiCall<any>(`/api/richieste-collaborazione/azienda`, "post", {
        body: buildMultiPartFormData(data),
        ...opts,
      }),
  };
};

export { richieste_collaborazione };
