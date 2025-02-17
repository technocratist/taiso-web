import { useState, useEffect } from "react";

interface FetchState<T> {
  data: T | null;
  loading: boolean;
  error: Error | null;
}

/**
 * API 호출을 위한 커스텀 훅.
 * @param fetcher - 데이터를 반환하는 Promise 함수
 * @param deps - 의존성 배열 (빈 배열이면 최초 마운트 시 1회 호출)
 */
const useFetch = <T>(
  fetcher: () => Promise<T>,
  deps: any[] = []
): FetchState<T> => {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    setLoading(true);
    fetcher()
      .then((result) => setData(result))
      .catch((err) => setError(err))
      .finally(() => setLoading(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, deps);

  return { data, loading, error };
};

export default useFetch;
