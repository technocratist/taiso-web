import { useState, useEffect } from "react";

interface FetchState<T> {
  data: T | null;
  loading: boolean;
  error: Error | null;
}

/**
 * API 호출을 위한 커스텀 훅.
 * @param fetcher - AbortSignal을 인자로 받는 Promise 함수
 * @param deps - 의존성 배열
 */
const useGet = <T>(
  fetcher: (signal: AbortSignal) => Promise<T>,
  deps: any[] = []
): FetchState<T> => {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    const controller = new AbortController();

    setLoading(true);
    fetcher(controller.signal)
      .then((result) => setData(result))
      .catch((err) => {
        // 취소된 요청은 에러로 처리하지 않음
        if (err.name !== "AbortError") {
          setError(err);
        }
      })
      .finally(() => setLoading(false));

    return () => {
      controller.abort();
    };
    // deps에 fetcher가 변경될 수 있다면 의존성 배열에 포함하세요.
  }, deps);

  return { data, loading, error };
};

export default useGet;
