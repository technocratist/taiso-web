import axios from "axios";
import { useEffect, useState } from "react";

interface Test {
  message: string;
}

function TestPage() {
  const [test, setTest] = useState<Test>({ message: "" });
  //test api 호출
  useEffect(() => {
    const testApi = async () => {
      const response = await axios.get("http://localhost:8080/api/route/1");
      setTest(response.data);
    };
    testApi();
  }, []);

  return <div>{test.message}</div>;
}

export default TestPage;
