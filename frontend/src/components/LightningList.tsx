function LightningList({ selectedDate }: { selectedDate: Date }) {
  return (
    <div>
      {selectedDate?.toDateString()}
      <div></div>
    </div>
  );
}

export default LightningList;
