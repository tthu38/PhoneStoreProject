document.addEventListener('DOMContentLoaded', function() {
    const profileBtn = document.getElementById('profileAvatarBtn');
    const profileModal = document.getElementById('profileModal');
    function openProfileModal() { profileModal.style.display = 'flex'; document.body.style.overflow = 'hidden'; }
    function closeProfileModal() { profileModal.style.display = 'none'; document.body.style.overflow = ''; }
    if (profileBtn && profileModal) {
        profileBtn.addEventListener('click', function(e) { e.preventDefault(); openProfileModal(); });
        profileModal.addEventListener('click', function(e) { if (e.target === profileModal) closeProfileModal(); });
        document.addEventListener('keydown', function(e) { if (e.key === 'Escape') closeProfileModal(); });
    }

    const openAuthModalBtn = document.getElementById('openAuthModalBtn');
    const authModal = document.getElementById('authModal');
    function openAuthModal() { if(authModal) { authModal.style.display = 'flex'; document.body.style.overflow = 'hidden'; } }
    function closeAuthModal() { if(authModal) { authModal.style.display = 'none'; document.body.style.overflow = ''; } }
    if (openAuthModalBtn && authModal) {
        openAuthModalBtn.addEventListener('click', function(e) { e.preventDefault(); openAuthModal(); });
        authModal.addEventListener('click', function(e) {
            if (e.target === authModal) closeAuthModal();
        });
        document.addEventListener('keydown', function(e) { if (e.key === 'Escape') closeAuthModal(); });
    }

    const closeAuthModalBtn = document.querySelector('.profile-modal-close');
    if (closeAuthModalBtn && authModal) {
        closeAuthModalBtn.addEventListener('click', function(e) {
            e.preventDefault();
            closeAuthModal();
        });
    }
});

function closeModal() {
  document.getElementById('modalId').style.display = 'none';
} 