import { useState } from "react";
import DateCarousel from "../components/DateCarousel";
import MainNavbar from "../components/MainNavbar";
import LightningList from "../components/LightningList";
import { Link } from "react-router";

function LightningPage() {
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());

  console.log(selectedDate);

  return (
    <div className="flex flex-col items-center justify-center gap-4">
      <MainNavbar />
      <Link to="/lightning/post" className="btn">
        루트 등록 test
      </Link>
      <DateCarousel onDateChange={(date) => setSelectedDate(date)} />
      <div>{selectedDate?.toDateString()}</div>
      <LightningList selectedDate={selectedDate} />
    </div>
  );
}

export default LightningPage;
