var container = document.getElementById('map'); // 지도를 담을 영역의 DOM 레퍼런스
var options = { // 지도를 생성할 때 필요한 기본 옵션
    center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표.
    level: 3 // 지도의 레벨(확대, 축소 정도)
};

var map = new kakao.maps.Map(container, options); // 지도 생성 및 객체 리턴
var geocoder = new kakao.maps.services.Geocoder();
// 전역 변수 선언
var polyline= null;
var distance = null;
var duration = null;
var selectedDepartureTime = null; // 사용자가 선택한 출발 예정 시간을 저장할 변수
var startAddress = null;
var destinationAddress = null;
var startMarker= null;
var destinationMarker= null;
async function drawRouteKakao(origin, destination, apiKey) {


   // const formattedDepartureTime = departureTime.toISOString(); // ISO 형식으로 출발 예정 시간 변환

    const REST_API_KEY = apiKey;
    console.log(apiKey);
    const url = 'https://apis-navi.kakaomobility.com/v1/directions';

    const headers = {
        Authorization: `KakaoAK ${REST_API_KEY}`,
        'Content-Type': 'application/json'
    };
    console.log("selectedDepartureTime ;:" + selectedDepartureTime);
    const queryParams = new URLSearchParams({

        origin: origin,
        destination: destination,
        avoid: "roadevent", // roadevent를 피하도록 설정
        departure_time: selectedDepartureTime // 출발 예정 시간 추가
    });
    console.log(queryParams);

    const requestUrl = `${url}?${queryParams}`;
    console.log(requestUrl);

    try{
        const response = await fetch(requestUrl, {
            method: 'GET',
            headers: headers
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();

        // 경로 데이터가 제대로 반환되었는지 확인
        if (!data || !data.routes || data.routes.length === 0 || !data.routes[0].sections || data.routes[0].sections.length === 0) {
            throw new Error("No valid route found.");
        }

        const linePath = [];
        data.routes[0].sections[0].roads.forEach(router => {
            router.vertexes.forEach((vertex, index) => {
                if (index % 2 === 0) {
                    linePath.push(new kakao.maps.LatLng(router.vertexes[index + 1], router.vertexes[index]));
                }
            });
        });
        polyline = new kakao.maps.Polyline({
            path: linePath,
            strokeWeight: 5,
            strokeColor: '#000000',
            strokeOpacity: 0.7,
            strokeStyle: 'solid'
        });
        polyline.setMap(map);

        console.log("data=" + data)
        // 거리와 소요 시간 가져오기
        distance = data.routes[0].summary.distance;
        duration = data.routes[0].summary.duration;

        console.log("distance1 == " + distance);
        console.log("distance1 == " + duration);

        displayRouteInfo(distance, duration);

    }catch (error){
        console.error("Error:", error);
    }
}

// 출발지부터 도착지까지 지도에 경로를 그리는 함수
async function drawRouteBetweenPoints(highwayNodes) {
    // 각 좌표를 담을 경로 배열
    const linePath = [];

    // 주어진 위도 경도 값을 순회하며 경로 배열에 추가
    for (const coords of highwayNodes) {
        console.log("Coords :: " + coords);
        const latLng = new kakao.maps.LatLng(coords[1], coords[0]);
        linePath.push(latLng);
    }

    // 지도에 표시될 선 생성
    const polyline = new kakao.maps.Polyline({
        path: linePath,
        strokeWeight: 5,
        strokeColor: '#000000',
        strokeOpacity: 0.7,
        strokeStyle: 'solid'
    });

    // 지도에 선 표시
    polyline.setMap(map);
}


// 주어진 경로들을 순회하면서 각각의 경로를 지도에 그림
async function drawRoutes(origin, destination, highwayNodes, apikey) {
    if (highwayNodes.length === 0) {
        // 고속도로 노드가 없는 경우 출발지에서 도착지까지의 경로만 그림
        await drawRouteKakao(origin, destination, apikey);
    } else if (highwayNodes.length === 1) {
        // 고속도로 노드가 1개인 경우 출발지에서 고속도로 노드까지의 경로와 고속도로 노드에서 도착지까지의 경로를 그림
        await drawRouteKakao(origin, highwayNodes[0], apikey);
        await drawRouteKakao(highwayNodes[0], destination, apikey);
    } else {
        // 고속도로 노드가 2개 이상인 경우
        await drawRouteKakao(origin, highwayNodes[0], apikey); // 출발지에서 첫 번째 고속도로 노드까지의 경로 그림
        for (let i = 0; i < highwayNodes.length - 1; i++) {
            await drawRouteKakao(highwayNodes[i], highwayNodes[i + 1], apikey); // 연속된 고속도로 노드들 간의 경로 그림
        }
        await drawRouteKakao(highwayNodes[highwayNodes.length - 1], destination, apikey); // 마지막 고속도로 노드부터 도착지까지의 경로 그림
    }
}


// 토글 버튼 클릭 시 작은 창을 열거나 닫습니다.
document.getElementById("toggle-button").addEventListener("click", function () {

    document.getElementById('search-container').classList.toggle('toggle-open');
    document.getElementById('search-container').classList.toggle('toggle-closed');

    // 토글 창이 열릴 때 출발지 및 도착지 입력란에 즉각적으로 자동 완성 기능 활성화
    if (document.getElementById('search-container').classList.contains('toggle-open')) {
        const startInput = document.getElementById('start-input-small');
        const destinationInput = document.getElementById('destination-input-small');

        const startSuggestionsContainer = document.getElementById('start-suggestions');
        const destinationSuggestionsContainer = document.getElementById('destination-suggestions');

        showSuggestions(startInput, startSuggestionsContainer);
        showSuggestions(destinationInput, destinationSuggestionsContainer);
    }
});
document.getElementById('close-button').addEventListener('click', function () {
    document.getElementById('search-container').classList.toggle('toggle-open');
    document.getElementById('search-container').classList.toggle('toggle-closed');
});

document.addEventListener("DOMContentLoaded", function() {
    var searchContainer = document.getElementById("search-container");
    var toggleButton = document.getElementById("toggle-button");

    // 검색창 표시 상태를 추적하는 변수
    var isSearchContainerVisible = false;

    // 토글 버튼 클릭 이벤트 리스너
    toggleButton.addEventListener("click", function() {
        // 검색창의 표시 상태를 토글
        isSearchContainerVisible = !isSearchContainerVisible;

        if (isSearchContainerVisible) {
            // 검색창을 보이게 하고 버튼 텍스트를 변경
            searchContainer.style.display = "block";
            toggleButton.innerHTML = '<img src="/image/toggleCloseButton.png" alt="닫기" />'; // 이미지로 변경
        } else {
            // 검색창을 숨기고 버튼 텍스트를 변경
            searchContainer.style.display = "none";
            toggleButton.innerHTML = '<img src="/image/toggleOpenButton.png" alt="열기" />'; // 이미지로 변경
        }
    });

    // 초기에 검색창을 숨김
    searchContainer.style.display = "none";
});
// 작은 창에서 출발지와 도착지 주소를 가져와 해당 위치에 마커를 추가합니다.
document.getElementById("search-form-small").addEventListener("submit", async function (event) {
    event.preventDefault();
    var apiKey = document.body.getAttribute('data-api-key');

    var startAddress = document.getElementById("start-input-small").value; // 출발지 주소 가져오기
    var destinationAddress = document.getElementById("destination-input-small").value; // 도착지 주소 가져오기

    // 서버로 출발지와 도착지 주소를 전송하여 좌표를 얻어옵니다.
    try {
        const response = await fetch('/api/search', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                startAddress: startAddress,
                destinationAddress: destinationAddress
            })
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        const startCoords = data.startCoords; // 출발지 좌표
        const destinationCoords = data.destinationCoords; // 도착지 좌표
        const distance = data.distance; // 거리
        selectedDepartureTime = getSelectedDepartureTime(); // 사용자가 선택한 출발 예정 시간 가져오기
        sendDepartureTimeToFastAPI(startCoords, destinationCoords, distance, selectedDepartureTime)

        console.log("selectedDepartureTime :::::" + selectedDepartureTime);
        console.log("Start Coordinates:", startCoords);
        console.log("Destination Coordinates:", destinationCoords);
        console.log("Distance:", distance);


        // 출발지와 도착지 좌표로 마커를 추가하고 경로를 표시합니다.
        //    addMarkerAndDrawRoute(startCoords, destinationCoords, apiKey);
        // 고속도로 노드들을 설정 (예시)
        const highwayNodes = [
            [127.01594195339248, 37.51631226093331],
            [127.10590011218926, 37.237307608483775],
            // [127.025065, 37.485538],
            // [127.037612, 37.465776],
            // [127.103686, 37.267513],
            [127.449390, 36.361496]  // 대전 ic
        ];
        const origin = `${startCoords.x},${startCoords.y}`;
        const destination = `${destinationCoords.x},${destinationCoords.y}`;

        addMarkersAndSetCenter(startCoords, destinationCoords);

        drawRoutes(origin, destination, highwayNodes, apiKey);
        resetSearch(); // 검색 필드 초기화

    } catch (error) {
        console.error("Error:", error);
        alert('경로 표시 중 오류가 발생했습니다.');
    }
});

// 검색창에 입력이 들어올 때마다 자동 완성을 표시하는 함수
function showSuggestions(input, suggestionsContainer, isStart) {
    // 검색창의 값 가져오기
    const inputValue = input.value;
    console.log("inputValue: " + inputValue);

    // 검색어가 없으면 자동 완성 목록을 숨김
    if (!inputValue) {
        suggestionsContainer.innerHTML = '';
        return;
    }

    // 서버에 자동 완성에 필요한 데이터를 요청
    fetch(`/api/suggestions?query=${inputValue}`)
        .then(response => response.json())
        .then(data => {

            showPlaceList(data, isStart);

        })
        .catch(error => {
            console.error('Error fetching suggestions:', error);
            suggestionsContainer.innerHTML = '';
        });

}
// 출발지 검색창에 입력이 들어올 때마다 자동 완성 기능 활성화
document.getElementById('start-input-small').addEventListener('input', function () {
    const suggestionsContainer = document.getElementById('start-suggestions');
    const  isStart = true;
    showSuggestions(this, suggestionsContainer, isStart);
});

// 도착지 검색창에 입력이 들어올 때마다 자동 완성 기능 활성화
document.getElementById('destination-input-small').addEventListener('input', function () {
    const suggestionsContainer = document.getElementById('destination-suggestions');
    const  isStart = false;

    showSuggestions(this, suggestionsContainer, isStart);

});
function resetSearch() {
    // 출발지 및 도착지 입력란 비우기
    var startCoords = null;
    var destinationCoords = null;
    var startAddress = null;
    var destinationAddress = null;

    // 이전에 추가된 마커 및 경로 제거
    if (startMarker) {
        startMarker.setMap(null); // 출발지 마커 제거
    }
    if (destinationMarker) {
        destinationMarker.setMap(null); // 도착지 마커 제거
    }
    if (polyline) {
        polyline.setMap(null); // 경로 제거
    }
    distance = null;
    duration = null;
}

// 경로 검색이 완료된 후에 실행되는 함수
function displayRouteInfo(distance, duration) {
    // 장소 리스트를 감싸고 있는 div 요소
    const placeListContainer = document.getElementById('place-list');

    // 장소 리스트를 삭제합니다.
    placeListContainer.innerHTML = '';

    // 거리와 시간을 표시할 새로운 요소를 생성합니다.
    const routeInfoElement = document.createElement('div');
    routeInfoElement.classList.add('place-info-box');

    // 거리를 표시합니다.
    const distanceInKm = (distance / 1000).toFixed(2); // 거리를 킬로미터 단위로 변환
    routeInfoElement.innerHTML += '<p>거리: ' + distanceInKm + 'km</p>';

    // 소요 시간을 시 분 단위로 변환하여 표시합니다.
    const hours = Math.floor(duration / 3600); // 소요 시간에서 시간 부분 추출
    const minutes = Math.floor((duration % 3600) / 60); // 소요 시간에서 분 부분 추출
    routeInfoElement.innerHTML += '<p>소요 시간: ' + hours + '시간 ' + minutes + '분</p>';

    // 새로운 요소를 장소 리스트를 감싸고 있는 div에 추가합니다.
    placeListContainer.appendChild(routeInfoElement);


}

// 사용자가 선택한 출발 예정 시간을 다른 함수에서 사용할 수 있도록 반환하는 함수
function getSelectedDepartureTime() {
    let currentDateTimeString;
    if (!selectedDepartureTime) {
        // 선택한 출발 예정 시간이 없는 경우 현재 시간을 YYYYMMDDHHMM 형식으로 반환합니다.
        const currentDate = new Date();
        const year = currentDate.getFullYear();
        const month = ('0' + (currentDate.getMonth() + 1)).slice(-2);
        const day = ('0' + currentDate.getDate()).slice(-2);
        const hours = ('0' + currentDate.getHours()).slice(-2);
        const minutes = ('0' + currentDate.getMinutes()).slice(-2);

        console.log("hours : " + hours)
        console.log("currentDate: " + currentDate);
        if (hours > 12) {
            currentDateTimeString = year + "년 " + month + "월 " + day + "일 오후 " + (hours - 12) + "시 " + minutes + "분 출발";
        } else if (hours === 12) {
            currentDateTimeString = year + "년 " + month + "월 " + day + "일 오후 " + hours + "시 " + minutes + "분 출발";
        } else if (hours === 0) {
            currentDateTimeString = year + "년 " + month + "월 " + day + "일 오전 12시 " + minutes + "분 출발";
        } else {
            currentDateTimeString = year + "년 " + month + "월 " + day + "일 오전 " + hours + "시 " + minutes + "분 출발";
        }
        //    document.getElementById("departure-time").value = currentDateTimeString;

        console.log("currentDateTimeString :" + currentDateTimeString);
        return `${year}${month}${day}${hours}${minutes}`;
    } else {
        return selectedDepartureTime;
    }
}

document.addEventListener("DOMContentLoaded", function() {
    // 현재 시간을 가져와서 표시
    var currentDate = new Date();
    console.log("currentDate : " + currentDate);
    var year = currentDate.getFullYear();
    var month = ('0' + (currentDate.getMonth() + 1)).slice(-2);
    var day = ('0' + currentDate.getDate()).slice(-2);
    var hours = ('0' + currentDate.getHours()).slice(-2);
    var minutes = ('0' + currentDate.getMinutes()).slice(-2);
    var ampm = currentDate.getHours() < 12 ? "오전" : "오후";
    console.log("currentDate: " + currentDate);
    console.log("hours : " + hours)

    var currentDateTimeString
    if (hours > 12) {
        currentDateTimeString = year + "년 " + month + "월 " + day + "일 오후 " + (hours - 12) + "시 " + minutes + "분 출발";
    } else if (hours === 12) {
        currentDateTimeString = year + "년 " + month + "월 " + day + "일 오후 " + hours + "시 " + minutes + "분 출발";
    } else if (hours === 0) {
        currentDateTimeString = year + "년 " + month + "월 " + day + "일 오전 12시 " + minutes + "분 출발";
    } else {
        currentDateTimeString = year + "년 " + month + "월 " + day + "일 오전 " + hours + "시 " + minutes + "분 출발";
    }
    console.log("currentDateTimeString :" + currentDateTimeString );

    flatpickr("#departure-time", {
        enableTime: true,
        minDate: "today", // 현재 날짜 이전 선택 불가능
        dateFormat: "Y년 n월 j일 H시 i분 출발", // 날짜 및 시간 표시 형식 설정
        defaultDate: currentDate, // 현재 시간을 default 값으로 설정
        time_24hr: false,
        onClose: function(selectedDates, dateStr, instance) {
            // 사용자가 시간을 선택한 후 실행되는 콜백 함수
            const selectedTime = instance.latestSelectedDateObj;
            const currentTime = new Date();
            console.log("selectedTime : " + selectedTime);
            if (selectedTime < currentTime) {
                // 선택한 시간이 현재 시간보다 이전인 경우 경고 메시지 표시
                alert("과거 시간은 선택할 수 없습니다.");
                instance.setDate(currentTime); // 현재 시간으로 재설정
            }
            else
            {
                const hours = selectedTime.getHours();
                const ampm = hours >= 12 ? "오후" : "오전";
                const formattedHours = hours % 12 || 12; // 12시간 형식으로 변환
                selectedDepartureTime = `${selectedTime.getFullYear()}년 ${selectedTime.getMonth() + 1}월 ${selectedTime.getDate()}일 ${ampm} ${formattedHours}시 ${selectedTime.getMinutes()}분 출발`;
                console.log("selectedDepartureTime2: " + selectedDepartureTime);
            }
        }

    })
});

// 서버로부터 받은 장소 정보를 표시하는 함수
function displayPlaceInfo(placeName, address, isStart) {
    const placeListContainer = document.getElementById('place-list');
    console.log("placeListContainer : " + placeListContainer);
    // 장소 정보를 담을 박스 생성
    const placeInfoBox = document.createElement('div');
    placeInfoBox.classList.add('place-info-box');

    // 장소 이름과 주소를 박스에 추가
    placeInfoBox.innerHTML = `
        <strong>${placeName}</strong><br>
        ${address}
    `;

    // 생성된 박스를 장소 목록 컨테이너에 추가
    placeListContainer.appendChild(placeInfoBox);

    // 장소 정보를 클릭했을 때 해당 위치를 출발점 또는 도착점으로 설정하는 이벤트 추가
    placeInfoBox.addEventListener('click', function() {
        const placeName = this.querySelector('strong').textContent; // 해당 위치의 주소값을 가져옵니다.
        console.log("Clicked placeName: " + placeName);
        const addressElement = this.querySelector('br').nextSibling; // 주소를 포함하는 요소를 선택합니다.
        const address = addressElement.textContent.trim(); // 해당 위치의 주소를 가져옵니다.
        console.log("Clicked address: " + address);

        // 클릭한 주소를 입력란에 넣기
        if (isStart) {
            document.getElementById('start-input-small').value = placeName; // 출발지 입력란에 클릭한 주소 이름을 넣습니다.
            startAddress = address;
        } else {
            document.getElementById('destination-input-small').value = placeName; // 도착지 입력란에 클릭한 주소 이름을 넣습니다.
            destinationAddress = address;
        }
    });
}


// 서버로부터 받은 데이터를 가지고 장소 정보 표시
function showPlaceList(data, isStart) {
    const placeListContainer = document.getElementById('place-list');
    placeListContainer.innerHTML = ''; // 기존 내용 비우기
    console.log("===placeListContainer : "+placeListContainer);
    // 받은 데이터를 기반으로 각 장소 정보를 표시
    for (const placeName in data) {
        if (data.hasOwnProperty(placeName)) {
            const address = data[placeName];
            console.log("===" + address)

            displayPlaceInfo(placeName, address, isStart);
        }
    }
}
// 출발지와 도착지에 마커를 추가하고 해당 위치를 지도 중심으로 설정하는 함수
function addMarkersAndSetCenter(startCoords, destinationCoords) {
    // 출발지와 도착지 좌표를 LatLng 객체로 변환
    const startLatLng = new kakao.maps.LatLng(startCoords.y, startCoords.x);
    const destinationLatLng = new kakao.maps.LatLng(destinationCoords.y, destinationCoords.x);

    console.log("startLatLng" + startLatLng);
    console.log("destinationLatLng" + destinationLatLng);

    // 출발지와 도착지 마커 생성
    const startMarker = new kakao.maps.Marker({
        map: map,
        position: startLatLng,
        title: '출발지'
    });
    const destinationMarker = new kakao.maps.Marker({
        map: map,
        position: destinationLatLng,
        title: '도착지'
    });

    // 출발지와 도착지 마커를 배열로 저장
    const markers = [startMarker, destinationMarker];

    // 출발지와 도착지 마커를 모두 표시하는 범위를 구합니다.
    const bounds = new kakao.maps.LatLngBounds();

    // 출발지와 도착지 마커를 모두 포함하도록 bounds에 추가
    for (const marker of markers) {
        bounds.extend(marker.getPosition());
    }

    // 출발지와 도착지 마커가 모두 보이도록 지도의 중심과 확대 수준을 설정합니다.
    map.setBounds(bounds);
}
async function sendDepartureTimeToFastAPI(startCoords, destinationCoords, distance, selectedDepartureTime) {
    try {

        const url = '/api/info'; // Spring Controller의 엔드포인트 URL

        const data = {
            startCoords: startCoords,
            destinationCoords: destinationCoords,
            distance: distance,
            departureTime: selectedDepartureTime
        };

        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            const responseData = await response.json();
            console.log('Response from FastAPI:', responseData);
        } else {
            console.error('Failed to send info to FastAPI. Status code:', response.status);
            const errorData = await response.text();
            console.error('Error message:', errorData);
        }
    } catch (error) {
        console.error('Error sending departure time to FastAPI:', error);
    }
}
