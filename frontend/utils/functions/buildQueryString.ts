export const buildQueryString = (params: any) => {
  const queryString = Object.keys(params)
    .filter((key) => {
      if (key === "searchField") {
        return (
          params["searchData"] !== undefined && params["searchData"] !== ""
        );
      }
      return params[key] !== "" && params[key] !== undefined;
    })
    .map(
      (key) => encodeURIComponent(key) + "=" + encodeURIComponent(params[key])
    )
    .join("&");
  return queryString;
};
